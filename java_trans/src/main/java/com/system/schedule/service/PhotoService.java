package com.system.schedule.service;

import com.system.schedule.entity.Photos;
import com.system.schedule.dto.PhotoHandleSearchParams;
import com.system.schedule.dto.PhotoHandleItem;
import com.system.schedule.dto.PhotoDetailItem;
import com.system.schedule.dto.PhotoUploadParams;
import com.system.schedule.util.ApiResult;
import com.system.schedule.util.ResultHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
public class PhotoService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private static final String PHOTOS_TABLE = "photos";
    
    // 获取头像列表
    @Transactional(readOnly = true)
    public ApiResult getPhotoListAsync(PhotoHandleSearchParams searchParams) {
        try {
            System.out.println("=== 开始查询头像列表 ===");
            System.out.println("搜索参数: Id=" + searchParams.getId() + ", Type=" + searchParams.getType());
            
            StringBuilder sql = new StringBuilder("SELECT * FROM " + PHOTOS_TABLE + " WHERE 1=1");
            List<Object> params = new ArrayList<>();
            
            // 构建查询条件
            if (StringUtils.hasText(searchParams.getId())) {
                sql.append(" AND id LIKE ?");
                params.add("%" + searchParams.getId() + "%");
            }
            
            if (StringUtils.hasText(searchParams.getType())) {
                sql.append(" AND type = ?");
                params.add(searchParams.getType());
            }
            
            List<Photos> photos = jdbcTemplate.query(
                sql.toString(),
                params.toArray(),
                new BeanPropertyRowMapper<>(Photos.class)
            );
            
            System.out.println("查询到 " + photos.size() + " 条记录");
            
            // 转换为DTO
            List<PhotoHandleItem> result = new ArrayList<>();
            for (Photos photo : photos) {
                PhotoHandleItem item = new PhotoHandleItem();
                item.setId(photo.getId() != null ? photo.getId() : "");
                item.setType(photo.getType() != null ? photo.getType() : "");
                item.setHasPhoto(photo.getPhoto() != null && photo.getPhoto().length > 0);
                item.setTypeDisplay(getTypeDisplay(photo.getType() != null ? photo.getType() : ""));
                result.add(item);
            }
            
            System.out.println("DTO转换结果数量: " + result.size());
            if (result.size() > 0) {
                PhotoHandleItem firstDto = result.get(0);
                System.out.println("第一条DTO数据: id='" + firstDto.getId() + 
                                  "', type='" + firstDto.getType() + 
                                  "', hasPhoto=" + firstDto.isHasPhoto());
            }
            
            System.out.println("=== 查询头像列表完成 ===");
            return ResultHelper.success(result);
        } catch (Exception ex) {
            System.out.println("查询头像列表异常: " + ex.getMessage());
            ex.printStackTrace();
            return ResultHelper.error("获取头像列表失败: " + ex.getMessage());
        }
    }
    
    // 获取头像详情
    @Transactional(readOnly = true)
    public ApiResult getPhotoDetailAsync(String id) {
        try {
            System.out.println("=== 开始获取头像详情: " + id + " ===");
            
            if (!StringUtils.hasText(id)) {
                return ResultHelper.error("头像ID不能为空");
            }
            
            String sql = "SELECT * FROM " + PHOTOS_TABLE + " WHERE id = ?";
            Photos photo = jdbcTemplate.queryForObject(
                sql,
                new BeanPropertyRowMapper<>(Photos.class),
                id
            );
            
            if (photo == null) {
                System.out.println("未找到ID为 " + id + " 的头像");
                return ResultHelper.error("头像不存在");
            }
            
            PhotoDetailItem result = new PhotoDetailItem();
            result.setId(photo.getId() != null ? photo.getId() : "");
            result.setType(photo.getType() != null ? photo.getType() : "");
            result.setPhotoBase64(photo.getPhoto() != null && photo.getPhoto().length > 0 ?
                Base64.getEncoder().encodeToString(photo.getPhoto()) : null);
            result.setTypeDisplay(getTypeDisplay(photo.getType() != null ? photo.getType() : ""));
            
            System.out.println("头像详情获取成功: id=" + result.getId() + 
                              ", 有图片=" + (result.getPhotoBase64() != null));
            System.out.println("=== 获取头像详情完成 ===");
            return ResultHelper.success(result);
        } catch (Exception ex) {
            System.out.println("获取头像详情异常: " + ex.getMessage());
            return ResultHelper.error("获取头像详情失败: " + ex.getMessage());
        }
    }
    
    // 上传/更新头像
    @Transactional
    public ApiResult uploadPhotoAsync(PhotoUploadParams uploadParams) {
        try {
            System.out.println("=== 开始上传头像: " + uploadParams.getId() + " ===");
            
            if (!StringUtils.hasText(uploadParams.getId())) {
                return ResultHelper.error("ID不能为空");
            }
            
            if (!StringUtils.hasText(uploadParams.getType())) {
                return ResultHelper.error("类型不能为空");
            }
            
            if (!isValidType(uploadParams.getType())) {
                return ResultHelper.error("类型必须是：学生、教师、其他");
            }
            
            // 转换Base64为字节数组
            byte[] photoBytes = null;
            if (StringUtils.hasText(uploadParams.getPhotoBase64())) {
                try {
                    photoBytes = Base64.getDecoder().decode(uploadParams.getPhotoBase64());
                } catch (Exception ex) {
                    System.out.println("Base64转换失败: " + ex.getMessage());
                    return ResultHelper.error("图片格式错误");
                }
            }
            
            // 检查是否已存在
            String checkSql = "SELECT COUNT(*) FROM " + PHOTOS_TABLE + " WHERE id = ?";
            int count = jdbcTemplate.queryForObject(checkSql, Integer.class, uploadParams.getId());
            
            if (count > 0) {
                // 更新现有头像
                String updateSql = "UPDATE " + PHOTOS_TABLE + " SET type = ?, photo = ? WHERE id = ?";
                int updateResult = jdbcTemplate.update(updateSql, 
                    uploadParams.getType(), photoBytes, uploadParams.getId());
                
                System.out.println("更新头像结果: 影响行数=" + updateResult);
                
                if (updateResult > 0) {
                    System.out.println("=== 头像更新成功 ===");
                    return ResultHelper.success("头像更新成功");
                } else {
                    return ResultHelper.error("头像更新失败");
                }
            } else {
                // 插入新头像
                String insertSql = "INSERT INTO " + PHOTOS_TABLE + " (id, type, photo) VALUES (?, ?, ?)";
                int insertResult = jdbcTemplate.update(insertSql,
                    uploadParams.getId(), uploadParams.getType(), photoBytes);
                
                System.out.println("插入头像结果: 影响行数=" + insertResult);
                
                if (insertResult > 0) {
                    System.out.println("=== 头像上传成功 ===");
                    return ResultHelper.success("头像上传成功");
                } else {
                    return ResultHelper.error("头像上传失败");
                }
            }
        } catch (Exception ex) {
            System.out.println("上传头像异常: " + ex.getMessage());
            ex.printStackTrace();
            return ResultHelper.error("上传头像失败: " + ex.getMessage());
        }
    }
    
    // 批量删除头像
    @Transactional
    public ApiResult batchDeleteAsync(List<String> ids) {
        try {
            System.out.println("=== 开始批量删除头像: " + String.join(",", ids) + " ===");
            
            if (ids == null || ids.isEmpty()) {
                return ResultHelper.error("请选择要删除的头像");
            }
            
            String placeholders = String.join(",", Arrays.asList("?", "?", "?")); // 简化处理
            String deleteSql = "DELETE FROM " + PHOTOS_TABLE + " WHERE id IN (" + placeholders + ")";
            int deleteResult = jdbcTemplate.update(deleteSql, ids.toArray());
            
            System.out.println("批量删除结果: 影响行数=" + deleteResult);
            
            if (deleteResult > 0) {
                System.out.println("=== 批量删除头像成功 ===");
                return ResultHelper.success("成功删除 " + deleteResult + " 个头像");
            } else {
                return ResultHelper.error("删除失败，未找到指定的头像");
            }
        } catch (Exception ex) {
            System.out.println("批量删除头像异常: " + ex.getMessage());
            return ResultHelper.error("批量删除失败: " + ex.getMessage());
        }
    }
    
    // 删除单个头像
    @Transactional
    public ApiResult deleteAsync(String id) {
        try {
            System.out.println("=== 开始删除头像: " + id + " ===");
            
            if (!StringUtils.hasText(id)) {
                return ResultHelper.error("头像ID不能为空");
            }
            
            String deleteSql = "DELETE FROM " + PHOTOS_TABLE + " WHERE id = ?";
            int deleteResult = jdbcTemplate.update(deleteSql, id);
            
            System.out.println("删除结果: 影响行数=" + deleteResult);
            
            if (deleteResult > 0) {
                System.out.println("=== 删除头像成功 ===");
                return ResultHelper.success("删除成功");
            } else {
                return ResultHelper.error("删除失败，头像不存在");
            }
        } catch (Exception ex) {
            System.out.println("删除头像异常: " + ex.getMessage());
            return ResultHelper.error("删除失败: " + ex.getMessage());
        }
    }
    
    // 从更新申请的头像数据更新到PHOTOS表
    @Transactional
    public ApiResult updatePhotoFromApplyAsync(String applicantId, String applicantType, String newPhotoBase64) {
        try {
            System.out.println("=== 开始从申请更新头像到PHOTOS表: " + applicantId + " ===");
            
            if (!StringUtils.hasText(applicantId)) {
                return ResultHelper.error("申请人ID不能为空");
            }
            
            if (!StringUtils.hasText(applicantType)) {
                return ResultHelper.error("申请人类型不能为空");
            }
            
            // 转换Base64为字节数组
            byte[] photoBytes = null;
            if (StringUtils.hasText(newPhotoBase64)) {
                try {
                    photoBytes = Base64.getDecoder().decode(newPhotoBase64);
                } catch (Exception ex) {
                    System.out.println("Base64转换失败: " + ex.getMessage());
                    return ResultHelper.error("头像数据格式错误");
                }
            }
            
            // 检查是否已存在
            String checkSql = "SELECT COUNT(*) FROM " + PHOTOS_TABLE + " WHERE id = ?";
            int count = jdbcTemplate.queryForObject(checkSql, Integer.class, applicantId);
            
            if (count > 0) {
                // 更新现有头像
                String updateSql = "UPDATE " + PHOTOS_TABLE + " SET type = ?, photo = ? WHERE id = ?";
                int updateResult = jdbcTemplate.update(updateSql, applicantType, photoBytes, applicantId);
                
                System.out.println("更新PHOTOS表结果: 影响行数=" + updateResult);
                
                if (updateResult > 0) {
                    System.out.println("=== 从申请更新头像成功 ===");
                    return ResultHelper.success("头像更新成功");
                } else {
                    return ResultHelper.error("头像更新失败");
                }
            } else {
                // 插入新头像记录
                String insertSql = "INSERT INTO " + PHOTOS_TABLE + " (id, type, photo) VALUES (?, ?, ?)";
                int insertResult = jdbcTemplate.update(insertSql, applicantId, applicantType, photoBytes);
                
                System.out.println("插入PHOTOS表结果: 影响行数=" + insertResult);
                
                if (insertResult > 0) {
                    System.out.println("=== 从申请插入头像成功 ===");
                    return ResultHelper.success("头像插入成功");
                } else {
                    return ResultHelper.error("头像插入失败");
                }
            }
        } catch (Exception ex) {
            System.out.println("从申请更新头像到PHOTOS表异常: " + ex.getMessage());
            return ResultHelper.error("更新头像失败: " + ex.getMessage());
        }
    }
    
    // 清空PHOTOS表中的头像数据
    @Transactional
    public ApiResult clearPhotoAsync(String applicantId) {
        try {
            System.out.println("=== 开始清空PHOTOS表头像: " + applicantId + " ===");
            
            if (!StringUtils.hasText(applicantId)) {
                return ResultHelper.error("申请人ID不能为空");
            }
            
            // 查找现有头像记录
            String checkSql = "SELECT COUNT(*) FROM " + PHOTOS_TABLE + " WHERE id = ?";
            int count = jdbcTemplate.queryForObject(checkSql, Integer.class, applicantId);
            
            if (count > 0) {
                // 将头像数据置为null
                String updateSql = "UPDATE " + PHOTOS_TABLE + " SET photo = NULL WHERE id = ?";
                int updateResult = jdbcTemplate.update(updateSql, applicantId);
                
                System.out.println("清空PHOTOS表头像结果: 影响行数=" + updateResult);
                
                if (updateResult > 0) {
                    System.out.println("=== 清空头像成功 ===");
                    return ResultHelper.success("头像清空成功");
                } else {
                    return ResultHelper.error("头像清空失败");
                }
            } else {
                System.out.println("PHOTOS表中不存在ID为 " + applicantId + " 的记录");
                return ResultHelper.success("用户头像记录不存在，无需清空");
            }
        } catch (Exception ex) {
            System.out.println("清空PHOTOS表头像异常: " + ex.getMessage());
            return ResultHelper.error("清空头像失败: " + ex.getMessage());
        }
    }
    
    // 获取类型显示文本
    private String getTypeDisplay(String type) {
        switch (type) {
            case "学生": return "学生";
            case "教师": return "教师";
            case "其他": return "其他";
            default: return "未知";
        }
    }
    
    // 验证类型是否有效
    private boolean isValidType(String type) {
        return "学生".equals(type) || "教师".equals(type) || "其他".equals(type);
    }
}
using System;

namespace Model.Dto.Profile
{
    public class ProfileDto
    {
        public string Id { get; set; } = string.Empty;
        public string Name { get; set; } = string.Empty;
        public string? NickName { get; set; }
        public int UserType { get; set; }
        public string? Email { get; set; }
        public string? MajorName { get; set; }
        public string? Telephone { get; set; }
        public int? StartYear { get; set; }
        public int? EndYear { get; set; }
        public decimal? Gpa { get; set; }
        public decimal? AttemptedCredit { get; set; }
        public decimal? EarnedCredit { get; set; }

        public int? WorkYear { get; set; }
        public string? Information { get; set; }

        // 新增：头像字段（Base64格式，可为null）
        public string? Avatar { get; set; }

        // 新增：教师专属字段（学生端返回null）
        public int? CourseCount { get; set; } // 当前授课数
        public int? StudentCount { get; set; } // 指导学生数
        public string? Title { get; set; } // 职称
    }
}
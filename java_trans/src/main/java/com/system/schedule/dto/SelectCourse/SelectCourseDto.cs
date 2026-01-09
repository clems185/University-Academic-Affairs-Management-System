namespace Model.Dto.SelectCourse
{
    public class SelectCourseDto
    {
        public string CourseId { get; set; } = null!;
        public string ClassId { get; set; } = "0"; // 默认"0"表示未选择班级
        public string CourseName { get; set; } = null!;
        public string? TeacherName { get; set; }
        public string? Location { get; set; }
        public string? TimeSlotInfo { get; set; } // 新增上课时间信息
        public string Status { get; set; } = "N";
        public int SelectedCount { get; set; }
        public int Capacity { get; set; }

        // 组合ID字段 (CourseId + ClassId)
        public string CourseClassId => $"{CourseId}{ClassId}";

        // 完整上课信息 (时间+地点)
        public string ClassInfo => $"{TimeSlotInfo} {Location}";
    }
}

namespace Model.Dto.SelectCourse
{
    public class ClassInfoDto
    {
        public string ClassId { get; set; } = null!;
        public string CourseId { get; set; } = null!;
        public string CourseName { get; set; } = null!;
        public string? TeacherName { get; set; }
        public string? Location { get; set; }
        public string? TimeSlotInfo { get; set; } // 新增上课时间信息
        public int SelectedCount { get; set; }
        public int Capacity { get; set; }

        // 组合ID字段 (CourseId + ClassId)
        public string CourseClassId => $"{CourseId}{ClassId}";

        // 完整上课信息 (时间+地点)
        public string ClassInfo => $"{TimeSlotInfo} {Location}";
    }
}
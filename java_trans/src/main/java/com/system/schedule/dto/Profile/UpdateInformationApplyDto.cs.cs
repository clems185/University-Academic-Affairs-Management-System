using System;

namespace Model.Dto.Profile
{
    public class UpdateInformationApplyDto
    {
        public string UserId { get; set; } = string.Empty;
        public string UserType { get; set; } = string.Empty;
        public string? NewProfile { get; set; }
        public string? NewPhoto { get; set; } // Base64格式
        public string Information { get; set; } = string.Empty; // 修改原因
    }
}
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Model.Dto.MyInformation
{
    public class MyInformationRes
    {
        public string InfoId { get; set; }
        public string Types { get; set; }
        public string Title { get; set; }
        public string Content { get; set; }
        public DateTime PublishTime { get; set; } = DateTime.Now;
    }
}

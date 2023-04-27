namespace BaiTapWebService.Domain.Tenants.VipPackage
{
    public class VipPackageEntity
    {
        public Guid VipPackageID { get; set; }
        public string? VipPackageName { get; set; }
        public int? MonthDuration { get; set; }
        public int? Price { get; set; }
    }
}

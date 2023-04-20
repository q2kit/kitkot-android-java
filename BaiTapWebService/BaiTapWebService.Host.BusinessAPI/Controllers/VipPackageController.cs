using BaiTapWebService.Application.Contracts.Tenants.VipPackage;
using BaiTapWebService.BusinessAPI.Apis;
using BaiTapWebService.Domain.Tenants.VipPackage;

namespace BaiTapWebService.BusinessApi.Controllers
{
    public class VipPackageController : BaseBusinessApi<IVipPackageService, Guid, VipPackageEntity, VipPackageEntity>
    {
        public VipPackageController(IVipPackageService service) : base(service)
        {
        }

    }
}

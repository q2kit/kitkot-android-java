using BaiTapWebService.Application.Bases;
using BaiTapWebService.Application.Contracts.Tenants.VipPackage;
using BaiTapWebService.Domain.Tenants.VipPackage;

namespace BaiTapWebService.Application.Tenants
{
    public class VipPackageService : CrudBaseService<IVipPackageRepo, Guid, VipPackageEntity, VipPackageEntity>, IVipPackageService
    {
        public VipPackageService(IServiceProvider serviceProvider) : base(serviceProvider)
        {
        }


    }
}

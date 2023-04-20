using BaiTapWebService.Application.Contracts.Bases;
using BaiTapWebService.Domain.Tenants.VipPackage;

namespace BaiTapWebService.Application.Contracts.Tenants.VipPackage
{
    public interface IVipPackageService : ICrudBaseService<Guid, VipPackageEntity, VipPackageEntity>
    {
    }
}

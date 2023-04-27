using BaiTapWebService.Application.Contracts.Bases;
using BaiTapWebService.Domain.Tenants.Order;

namespace BaiTapWebService.Application.Contracts.Tenants.Order
{
    public interface ISuperUserService : ICrudBaseService<Guid, SuperUserEntity, SuperUserEntity>
    {
        Task<string> GetTokenPlp(Guid vipPackageID);
        Task<int> CheckSuperUser(int userID);
    }
}

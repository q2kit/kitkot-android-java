using BaiTapWebService.Domain.Repos;

namespace BaiTapWebService.Domain.Tenants.Order
{
    public interface ISuperUserRepo : IBaseRepo
    {
        Task<int> GetOrderStatus(string orderCode);
        Task<int> CheckSuperUser(int userID);
    }
}

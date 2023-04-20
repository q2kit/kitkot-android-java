using BaiTapWebService.Domain.Tenants.Order;
using BapTapWebService.Domain.Mssql.Base;

namespace BapTapWebService.Domain.Mssql.Tenants
{
    public class MssqlSuperUserRepo : MssqlBaseTenantRepo, ISuperUserRepo
    {
        public async Task<int> CheckSuperUser(int userID)
        {
            var data = (int)await Provider.ExecuteScalarAsync("dbo.CheckSuperUser", new
            {
                UserID = userID
            });
            return data;
        }

        public async Task<int> GetOrderStatus(string orderCode)
        {
            var data = await Provider.QueryStoredProcedureAsync("dbo.Proc_GetOrderStatus", new
            {
                OrderCode = orderCode
            });
            return 1;
        }
    }
}

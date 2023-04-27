using System.Data;

namespace BaiTapWebService.Domain.Mysql.Providers
{
    public interface IMysqlDatabaseProvider
    {
        string GetConnectionString();
        IDbConnection GetConnection();
        IDbConnection GetOpenConnection();
        void CloseConnection(IDbConnection connection);
        Task<List<T>> QueryStoredProcedureAsync<T>(string storeName, object param = null);
        Task<List<IDictionary<string, object>>> QueryStoredProcedureAsync(string storeName, object param = null);
        Task<List<T>> QueryStoredProcedureAsync<T>(string storeName, object param, IDbConnection connection = null, IDbTransaction transaction = null);
    }
}

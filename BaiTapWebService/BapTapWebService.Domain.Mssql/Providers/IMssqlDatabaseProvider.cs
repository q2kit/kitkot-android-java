using System.Data;

namespace BapTapWebService.Domain.Mssql.Providers
{
    public interface IMssqlDatabaseProvider
    {
        string GetConnectionString();
        IDbConnection GetConnection();
        IDbConnection GetOpenConnection();
        void CloseConnection(IDbConnection connection);
        Task<List<T>> QueryStoredProcedureAsync<T>(string storeName, object param = null);
        Task<List<IDictionary<string, object>>> QueryStoredProcedureAsync(string storeName, object param = null);
        Task<List<T>> QueryStoredProcedureAsync<T>(string storeName, object param, IDbConnection connection = null, IDbTransaction transaction = null);

        Task<int> ExecuteNonQueryTextAsync(string commandText, Dictionary<string, object> param, int? timeout = null);

        Task<object> ExecuteScalarTextAsync(string commandText, IDictionary<string, object> param);
        Task<object> ExecuteScalarAsync(string storeName, object param);


        Task<List<T>> QueryAsync<T>(string commandText, Dictionary<string, object> param);

    }
}

using Dapper;
using System.Data;
using System.Data.SqlClient;

namespace BapTapWebService.Domain.Mssql.Providers
{
    public class MssqlProvider : IMssqlDatabaseProvider
    {
        #region Fields
        private readonly string _connectionString;
        #endregion
        public MssqlProvider(string connectionString)
        {
            _connectionString = connectionString;
        }
        public void CloseConnection(IDbConnection connection)
        {
            if (connection != null)
            {
                connection.Close();
                connection.Dispose();
            }
        }

        public Task<int> ExecuteNonQueryTextAsync(string commandText, Dictionary<string, object> param, int? timeout = null)
        {
            throw new NotImplementedException();
        }

        public Task<object> ExecuteScalarTextAsync(string commandText, IDictionary<string, object> param)
        {
            throw new NotImplementedException();
        }

        public IDbConnection GetConnection()
        {
            var cnn = new SqlConnection(_connectionString);
            return cnn;
        }

        public string GetConnectionString()
        {
            return _connectionString;
        }

        public IDbConnection GetOpenConnection()
        {
            var cnn = GetConnection();
            cnn.Open();
            return cnn;

        }

        public Task<List<T>> QueryAsync<T>(string commandText, Dictionary<string, object> param)
        {
            throw new NotImplementedException();
        }

        public async Task<List<T>> QueryStoredProcedureAsync<T>(string storeName, object param = null)
        {
            using (var connection = GetOpenConnection())
            {
                return await QueryStoredProcedureAsync<T>(storeName, param, connection);
            }
        }

        public async Task<List<T>> QueryStoredProcedureAsync<T>(string storeName, object param, IDbConnection connection = null, IDbTransaction transaction = null)
        {
            var command = BuidCommandDefinition(storeName, CommandType.Text, param, transaction);
            var cnn = transaction != null ? transaction.Connection : connection;
            var result = await cnn.QueryAsync<T>(command);
            return result.ToList();
        }

        public async Task<List<IDictionary<string, object>>> QueryStoredProcedureAsync(string storeName, object param = null)
        {
            using (var connection = GetOpenConnection())
            {
                return await this.QueryStoredProcedureAsync(storeName, param, connection);
            }
        }

        public async Task<List<IDictionary<string, object>>> QueryStoredProcedureAsync(string storeName, object param = null, IDbConnection connection = null, IDbTransaction transaction = null)
        {
            var command = BuidCommandDefinition(storeName, CommandType.StoredProcedure, param, transaction);
            var cnn = transaction != null ? transaction.Connection : connection;
            var result = await cnn.QueryAsync(command);
            return result.Cast<IDictionary<string, object>>().ToList();
        }


        private CommandDefinition BuidCommandDefinition(string sql, CommandType commandType, object param, IDbTransaction transaction)
        {
            if (commandType == CommandType.Text)
            {
                sql = ProcessSqlBeforeExecute(sql);
            }
            return new CommandDefinition(sql, commandType: commandType, parameters: param, transaction: transaction);
        }

        public string ProcessSqlBeforeExecute(string sql)
        {
            //sql injection
            return sql;
        }

        public async Task<object> ExecuteScalarAsync(string storeName, object param)
        {
            using (var connection = GetOpenConnection())
            {
                return await this.ExecuteScalarAsync(storeName, param, connection);
            }

        }
        public async Task<object> ExecuteScalarAsync(string storeName, object param = null, IDbConnection connection = null, IDbTransaction transaction = null)
        {
            var command = BuidCommandDefinition(storeName, CommandType.StoredProcedure, param, transaction);
            var cnn = transaction != null ? transaction.Connection : connection;
            var result = await cnn.ExecuteScalarAsync(command);
            return result;
        }
    }
}

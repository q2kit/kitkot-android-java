using Dapper;
using MySqlConnector;
using System.Data;

namespace BaiTapWebService.Domain.Mysql.Providers
{
    public class MysqlProvider : IMysqlDatabaseProvider
    {
        #region Fields
        private readonly string _connectionString;
        #endregion
        public MysqlProvider(string connectionString)
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




        public IDbConnection GetConnection()
        {
            var cnn = new MySqlConnection(_connectionString);
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

        public async Task<List<T>> QueryStoredProcedureAsync<T>(string storeName, object param = null)
        {
            using (var connection = GetOpenConnection())
            {
                return await QueryStoredProcedureAsync<T>(storeName, param, connection);
            }
        }

        public async Task<List<IDictionary<string, object>>> QueryStoredProcedureAsync(string storeName, object param = null)
        {
            using (var connection = GetOpenConnection())
            {
                return await this.QueryStoredProcedureAsync(storeName, param, connection);
            }
        }

        public async Task<List<T>> QueryStoredProcedureAsync<T>(string storeName, object param, IDbConnection connection = null, IDbTransaction transaction = null)
        {
            var command = BuidCommandDefinition(storeName, CommandType.Text, param, transaction);
            var cnn = transaction != null ? transaction.Connection : connection;
            var result = await cnn.QueryAsync<T>(command);
            return result.ToList();
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

    }
}

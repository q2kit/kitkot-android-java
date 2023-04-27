using BaiTapWebService.Domain.Repos;
using BapTapWebService.Domain.Mssql.Providers;
using System.Data;

namespace BapTapWebService.Domain.Mssql.Base
{
    public class MssqlBaseRepo : IBaseRepo
    {
        #region Fields
        private string _connectionString;
        private IMssqlDatabaseProvider _dbProvider;
        #endregion

        public MssqlBaseRepo(string connectionString)
        {
            _connectionString = connectionString;
        }

        #region Properties
        protected IMssqlDatabaseProvider Provider
        {
            get
            {

                if (_dbProvider == null)
                {
                    _dbProvider = this.CreateProvider(_connectionString);
                }

                return _dbProvider;


            }
        }
        #endregion


        #region Methods
        protected virtual IMssqlDatabaseProvider CreateProvider(string connectionString)
        {
            return new MssqlProvider(connectionString);
        }
        public void CloseConnection(IDbConnection connection)
        {
            this.Provider.CloseConnection(connection);
        }

        public IDbConnection GetOpenConnection()
        {
            var cnn = this.Provider.GetConnection();
            cnn.Open();
            return cnn;
        }

        public async Task<IList<TEntity>> GetAllAsync<TEntity>()
        {
            string storeName = "Proc_" + typeof(TEntity).Name + "_GetAll";
            var data = await Provider.QueryStoredProcedureAsync<TEntity>(storeName);
            return data.ToList();
        }
        #endregion
    }
}

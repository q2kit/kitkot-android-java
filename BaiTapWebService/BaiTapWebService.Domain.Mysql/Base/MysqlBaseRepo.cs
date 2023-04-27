using BaiTapWebService.Domain.Mysql.Providers;
using BaiTapWebService.Domain.Repos;
using System.Data;

namespace BaiTapWebService.Domain.Mysql.Base
{
    public class MysqlBaseRepo : IBaseRepo
    {
        private string _connectionString;
        private IMysqlDatabaseProvider _dbProvider;
        public MysqlBaseRepo(string connectionString)
        {
            _connectionString = connectionString;
        }
        protected IMysqlDatabaseProvider Provider
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
        protected virtual IMysqlDatabaseProvider CreateProvider(string connectionString)
        {
            return new MysqlProvider(connectionString);
        }

        public void CloseConnection(IDbConnection connection)
        {
            throw new NotImplementedException();
        }

        public Task<IList<TEntity>> GetAllAsync<TEntity>()
        {
            throw new NotImplementedException();
        }

        public IDbConnection GetOpenConnection()
        {
            throw new NotImplementedException();
        }
    }
}

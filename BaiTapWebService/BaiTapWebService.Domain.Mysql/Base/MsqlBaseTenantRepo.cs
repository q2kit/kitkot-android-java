using BaiTapWebService.Domain.Mysql.Providers;

namespace BaiTapWebService.Domain.Mysql.Base
{
    public class MsqlBaseTenantRepo : MysqlBaseRepo
    {
        public MsqlBaseTenantRepo() : base(null)
        {
        }
        protected override IMysqlDatabaseProvider CreateProvider(string connectionString)
        {
            var connection = connectionString ?? this.GetConnectionString();
            return new MysqlProvider(connection);
        }

        protected virtual string GetConnectionString()
        {
            //#if DEBUG
            // return "Server=kitkot.mysql.q2k.dev;Port=3306;Database=kitkot;Uid=kitkot;Pwd=kitkot123@;";
            //#endif
            return "Server=localhost;Port=3307;Database=misacegov_development;Uid=root;Pwd=123;";

        }
    }
}

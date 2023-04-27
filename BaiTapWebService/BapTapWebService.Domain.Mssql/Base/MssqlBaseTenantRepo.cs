using BapTapWebService.Domain.Mssql.Providers;

namespace BapTapWebService.Domain.Mssql.Base
{
    public abstract class MssqlBaseTenantRepo : MssqlBaseRepo
    {
        public MssqlBaseTenantRepo() : base(null)
        {
        }
        protected override IMssqlDatabaseProvider CreateProvider(string connectionString)
        {
            var connection = connectionString ?? this.GetConnectionString();
            return new MssqlProvider(connection);
        }

        protected virtual string GetConnectionString()
        {
            //#if DEBUG
            return "Server=NPTINH\\NPT;Database=nptinh_webservice;User id=sa;Password=123";
            //#endif
        }
    }
}

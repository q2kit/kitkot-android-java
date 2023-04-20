using BaiTapWebService.Domain.Tenants.Video;
using BapTapWebService.Domain.Mssql.Base;

namespace BaiTapWebService.Domain.Mysql.Tenants
{
    public class MysqlVideoRepo : MssqlBaseTenantRepo, IVideoRepo
    {
    }
}

using BaiTapWebService.Application;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace BaiTapWebService.HostBase
{
    public class HostBaseFactory
    {
        /// <summary>
        /// 
        /// 
        /// </summary>
        /// <param name="services"></param>
        /// <param name="configuration"></param>
        public static void InjectMasterService(IServiceCollection services, IConfiguration configuration)
        {
            //service
            ApplicationFactory.InjectMasterService(services, configuration);
        }



        /// <summary>
        /// Khởi tạo repo thao tác với db nghiệp vụ
        /// </summary>
        /// <param name="services"></param>
        /// <param name="configuration"></param>
        public static void InjectTenantService(IServiceCollection services, IConfiguration configuration)
        {
            //service
            ApplicationFactory.InjectTenantService(services, configuration);
        }
    }
}
using BaiTapWebService.Application.Contracts.Master;
using BaiTapWebService.Application.Master;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace BaiTapWebService.Application
{
    public static class ApplicationFactory
    {

        public static void InjectMasterService(IServiceCollection services, IConfiguration configuration)
        {
            services.AddSingleton<ITenantService, TenantService>();

        }

        public static void InjectTenantService(IServiceCollection services, IConfiguration configuration)
        {
            //services.AddScoped<IOrderService, SuperUserService>();

        }
    }
}
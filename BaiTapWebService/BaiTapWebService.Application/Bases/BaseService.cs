using BaiTapWebService.Application.Contracts.Bases;
using BaiTapWebService.Domain.Repos;
using Microsoft.Extensions.DependencyInjection;

namespace BaiTapWebService.Application.Bases
{
    public class BaseService<TRepo> : IBaseService
        where TRepo : IBaseRepo
    {
        protected TRepo _repo;
        public BaseService(IServiceProvider serviceProvider)
        {
            _repo = serviceProvider.GetService<TRepo>();
        }
    }
}

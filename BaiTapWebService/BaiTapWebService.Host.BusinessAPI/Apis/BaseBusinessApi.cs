using BaiTapWebService.Application.Contracts.Bases;
using BaiTapWebService.HostBase.Apis;
using Microsoft.AspNetCore.Mvc;

namespace BaiTapWebService.BusinessAPI.Apis
{
    public abstract class BaseBusinessApi<TService, TKey, TEntity, TEntityDtoEdit> : BaseApi
        where TService : ICrudBaseService<TKey, TEntity, TEntityDtoEdit>
    {
        protected readonly TService _service;

        public BaseBusinessApi(TService service)
        {
            _service = service;
        }

        [HttpGet]
        public async Task<IActionResult> GetAllAsync()
        {
            var res = await _service.GetAllAsync();
            return Ok(res);
        }
    }
}

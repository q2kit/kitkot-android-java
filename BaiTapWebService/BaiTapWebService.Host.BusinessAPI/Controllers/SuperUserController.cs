using BaiTapWebService.Application.Contracts.Tenants.Order;
using BaiTapWebService.BusinessAPI.Apis;
using BaiTapWebService.Domain.Tenants.Order;
using BaiTapWebService.Domain.Tenants.VipPackage;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;

namespace BaiTapWebService.BusinessAPI.Controllers
{
    public class SuperUserController : BaseBusinessApi<ISuperUserService, Guid, SuperUserEntity, SuperUserEntity>
    {
        public SuperUserController(ISuperUserService service) : base(service)
        {
        }
        /// <summary>
        /// Lấy token từ zlp
        /// </summary>
        /// <param name="orderCode"></param>
        /// <returns></returns>
        [HttpPost("GetToken")]
        public async Task<IActionResult> GetTokenPlp([FromBody] VipPackageEntity vipPackage)
        {
            string token = await _service.GetTokenPlp(vipPackage.VipPackageID);

            return Ok(JsonConvert.SerializeObject(new
            {
                token = token
            }));
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="userID"></param>
        /// <returns>
        /// 0: Chưa từng đăng ký
        /// 1: Đang là superuser
        /// 2: Hết hạn
        /// </returns>
        [HttpPost("CheckSuperUser")]
        public async Task<IActionResult> CheckSuperUser(int userID)
        {
            var status = await _service.CheckSuperUser(userID);
            return Ok(status);
        }

        [HttpPost("register")]
        public async Task<IActionResult> Register(int userID)
        {
            var status = await _service.CheckSuperUser(userID);
            return Ok(status);
        }

    }
}

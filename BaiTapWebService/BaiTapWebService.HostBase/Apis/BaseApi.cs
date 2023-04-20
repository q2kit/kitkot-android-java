using Microsoft.AspNetCore.Mvc;

namespace BaiTapWebService.HostBase.Apis
{
    [ApiController]
    [Route("[controller]")]
    public abstract class BaseApi : ControllerBase
    {
    }
}

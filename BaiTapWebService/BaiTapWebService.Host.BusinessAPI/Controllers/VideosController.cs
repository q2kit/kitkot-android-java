using BaiTapWebService.Application.Contracts.Tenants.Video;
using BaiTapWebService.BusinessAPI.Apis;
using BaiTapWebService.Domain.Tenants.Video;

namespace BaiTapWebService.BusinessApi.Controllers
{
    public class VideosController : BaseBusinessApi<IVideoService, Guid, VideoEntity, VideoEntity>
    {
        public VideosController(IVideoService service) : base(service)
        {
        }
    }
}

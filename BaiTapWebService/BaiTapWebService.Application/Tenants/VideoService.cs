using BaiTapWebService.Application.Bases;
using BaiTapWebService.Application.Contracts.Tenants.Video;
using BaiTapWebService.Domain.Tenants.Video;

namespace BaiTapWebService.Application.Tenants
{
    public class VideoService : CrudBaseService<IVideoRepo, Guid, VideoEntity, VideoEntity>, IVideoService
    {
        public VideoService(IServiceProvider serviceProvider) : base(serviceProvider)
        {
        }
    }
}

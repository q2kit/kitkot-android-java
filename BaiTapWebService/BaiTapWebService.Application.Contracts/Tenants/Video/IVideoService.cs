using BaiTapWebService.Application.Contracts.Bases;
using BaiTapWebService.Domain.Tenants.Video;

namespace BaiTapWebService.Application.Contracts.Tenants.Video
{
    public interface IVideoService : ICrudBaseService<Guid, VideoEntity, VideoEntity>
    {
    }
}

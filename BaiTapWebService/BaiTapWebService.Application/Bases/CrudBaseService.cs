using BaiTapWebService.Application.Contracts.Bases;
using BaiTapWebService.Domain.Repos;

namespace BaiTapWebService.Application.Bases
{
    public class CrudBaseService<TRepo, TKey, TEntity, TEntityToEdit> : BaseService<TRepo>, ICrudBaseService<TKey, TEntity, TEntityToEdit>
        where TRepo : IBaseRepo
        where TEntityToEdit : TEntity
    {
        public CrudBaseService(IServiceProvider serviceProvider) : base(serviceProvider)
        {
        }

        public async Task<IList<TEntity>> GetAllAsync()
        {
            return await _repo.GetAllAsync<TEntity>();
        }
    }
}

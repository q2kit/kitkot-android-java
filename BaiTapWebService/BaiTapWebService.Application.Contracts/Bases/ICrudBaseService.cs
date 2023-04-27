namespace BaiTapWebService.Application.Contracts.Bases
{
    public interface ICrudBaseService<TKey, TEntity, TEntityDtoEdit> : IBaseService
    {
        Task<IList<TEntity>> GetAllAsync();
    }
}

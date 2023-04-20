using System.Data;

namespace BaiTapWebService.Domain.Repos
{
    public interface IBaseRepo
    {
        IDbConnection GetOpenConnection();
        void CloseConnection(IDbConnection connection);

        Task<IList<TEntity>> GetAllAsync<TEntity>();

    }
}

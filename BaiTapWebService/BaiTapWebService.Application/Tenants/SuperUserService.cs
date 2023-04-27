using BaiTapWebService.Application.Bases;
using BaiTapWebService.Application.Contracts.Tenants.Order;
using BaiTapWebService.Domain.Shared.Constants;
using BaiTapWebService.Domain.Shared.Helpers;
using BaiTapWebService.Domain.Tenants.Order;
using Newtonsoft.Json.Linq;
using System.Net;
using System.Security.Authentication;
using ZaloPay.Helper;
using ZaloPay.Helper.Crypto;

namespace BaiTapWebService.Application.Tenants
{
    public class SuperUserService : CrudBaseService<ISuperUserRepo, Guid, SuperUserEntity, SuperUserEntity>, ISuperUserService
    {
        public SuperUserService(IServiceProvider serviceProvider) : base(serviceProvider)
        {
        }

        public async Task<string> GetTokenPlp(Guid vipPackageID)
        {

            var transid = Guid.NewGuid().ToString();
            var embeddata = "{}";
            var items = "[]";
            var param = new Dictionary<string, string>();

            param.Add("app_id", ZaloPayAppInfo.APP_ID.ToString());
            param.Add("app_user", "Android_Demo");
            param.Add("app_time", Utils.GetTimeStamp().ToString());
            param.Add("amount", "2000");
            param.Add("app_trans_id", ZaloPayHelper.GetAppTransId());
            param.Add("embed_data", "{}");
            param.Add("item", "[]");
            param.Add("description", "ZaloPay demo");
            param.Add("bank_code", "zalopayapp");

            var data = ZaloPayAppInfo.APP_ID.ToString() + "|" + param["app_trans_id"] + "|" + param["app_user"] + "|" + param["amount"] + "|"
                + param["app_time"] + "|" + param["embed_data"] + "|" + param["item"];
            param.Add("mac", HmacHelper.Compute(ZaloPayHMAC.HMACSHA256, ZaloPayAppInfo.MAC_KEY, data));

            var handler = new HttpClientHandler()
            {
                SslProtocols = SslProtocols.Tls12,
                ServerCertificateCustomValidationCallback = (sender, cert, chain, sslPolicyErrors) => true,
            };

            ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12;
            ServicePointManager.Expect100Continue = false;
            ServicePointManager.DefaultConnectionLimit = 10000;

            var client = new HttpClient(handler)
            {
                BaseAddress = new Uri(ZaloPayAppInfo.URL_CREATE_ORDER),
                Timeout = TimeSpan.FromSeconds(5),
            };


            var content = new FormUrlEncodedContent(new[]
            {
                new KeyValuePair<string, string>("app_id", param["app_id"]),
                new KeyValuePair<string, string>("app_user", param["app_user"]),
                new KeyValuePair<string, string>("app_time", param["app_time"]),
                new KeyValuePair<string, string>("amount", param["amount"]),
                new KeyValuePair<string, string>("app_trans_id", param["app_trans_id"]),
                new KeyValuePair<string, string>("embed_data", param["embed_data"]),
                new KeyValuePair<string, string>("item", param["item"]),
                new KeyValuePair<string, string>("description", param["description"]),
                new KeyValuePair<string, string>("bank_code", param["bank_code"]),
                new KeyValuePair<string, string>("mac", param["mac"]),
            });

            var request = new HttpRequestMessage(HttpMethod.Post, ZaloPayAppInfo.URL_CREATE_ORDER)
            {
                Content = content,
            };

            var response = await client.SendAsync(request);
            if (response.IsSuccessStatusCode)
            {

                var responseBody = await response.Content.ReadAsStringAsync();
                JObject json = JObject.Parse(responseBody);
                if ((int)json.GetValue("return_code") == 1)
                {
                    return (string)json.GetValue("zp_trans_token");
                }
            }
            return null;
        }
        public bool RegisterSuperUser()
        {
            return true;
        }

        public async Task<int> CheckSuperUser(int userID)
        {
            return await _repo.CheckSuperUser(userID);
        }

    }
}

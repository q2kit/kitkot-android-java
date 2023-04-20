//using BaiTapWebService.Domain.Shared.Constants;
//using Newtonsoft.Json;
//using System.Text;

//namespace BaiTapWebService.Domain.Shared.Helpers
//{
//    public static class HttpHelper
//    {
//        //private static readonly string apiBasicUri = "http://localhost:8080/";
//        private static readonly string apiBasicUri = ZaloPayAppInfo.URL;

//        public static async Task Post<T>(string url, T contentValue)
//        {
//            using (var client = new HttpClient())
//            {
//                client.BaseAddress = new Uri(apiBasicUri);
//                var content = new StringContent(JsonConvert.SerializeObject(contentValue), Encoding.UTF8, "application/x-www-form-urlencoded");
//                var result = await client.PostAsync(url, content);
//                result.EnsureSuccessStatusCode();
//            }
//        }

//        public static async Task Put<T>(string url, T stringValue)
//        {
//            using (var client = new HttpClient())
//            {
//                client.BaseAddress = new Uri(apiBasicUri);
//                var content = new StringContent(JsonConvert.SerializeObject(stringValue), Encoding.UTF8, "application/json");
//                var result = await client.PutAsync(url, content);
//                result.EnsureSuccessStatusCode();
//            }
//        }

//        public static async Task<T> Get<T>(string url)
//        {
//            using (var client = new HttpClient())
//            {
//                client.BaseAddress = new Uri(apiBasicUri);
//                var result = await client.GetAsync(url);
//                result.EnsureSuccessStatusCode();
//                string resultContentString = await result.Content.ReadAsStringAsync();
//                T resultContent = JsonConvert.DeserializeObject<T>(resultContentString);
//                return resultContent;
//            }
//        }

//        public static async Task Delete(string url)
//        {
//            using (var client = new HttpClient())
//            {
//                client.BaseAddress = new Uri(apiBasicUri);
//                var result = await client.DeleteAsync(url);
//                result.EnsureSuccessStatusCode();
//            }
//        }
//    }
//}

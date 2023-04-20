using System.Security.Cryptography;
using System.Text;

namespace BaiTapWebService.Domain.Shared.Helpers
{
    public static class ZaloPayHelper
    {
        private static int _transIdDefault = 1;
        public static string GetAppTransId()
        {
            if (_transIdDefault >= 100000)
            {
                _transIdDefault = 1;
            }

            _transIdDefault += 1;
            DateTime now = DateTime.Now;
            string timeString = now.ToString("yyMMdd_hhmmss");
            return string.Format("{0}{1:D6}", timeString, _transIdDefault);
        }
        public static string GetMac(string key, string data)
        {
            byte[] keyBytes = Encoding.UTF8.GetBytes(key);
            byte[] dataBytes = Encoding.UTF8.GetBytes(data);

            using (HMACSHA256 hmac = new HMACSHA256(keyBytes))
            {
                byte[] hashBytes = hmac.ComputeHash(dataBytes);
                return BitConverter.ToString(hashBytes).Replace("-", "").ToLower();
            }
        }
    }
}

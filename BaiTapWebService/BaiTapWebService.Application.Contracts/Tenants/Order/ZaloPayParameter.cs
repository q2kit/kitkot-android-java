using BaiTapWebService.Domain.Shared.Constants;
using BaiTapWebService.Domain.Shared.Helpers;

namespace BaiTapWebService.Application.Contracts.Tenants.Order
{
    public class ZaloPayParameter
    {
        public string app_id;
        public string app_user;
        public string app_time;
        public string amount;
        public string app_trans_id;
        public string embed_data;
        public string item;
        public string bank_code;
        public string description;
        public string mac;

        public string GetMac()
        {
            string inputHMac = string.Format("{0}|{1}|{2}|{3}|{4}|{5}|{6}",
                   this.app_id,
                   this.app_trans_id,
                   this.app_user,
                   this.amount,
                   this.app_time,
                   this.embed_data,
                   this.item);
            return ZaloPayHelper.GetMac(ZaloPayAppInfo.MAC_KEY, inputHMac);
        }
    }
}

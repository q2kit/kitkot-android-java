using BaiTapWebService.Application.Contracts.Tenants.Order;
using BaiTapWebService.Application.Contracts.Tenants.Video;
using BaiTapWebService.Application.Contracts.Tenants.VipPackage;
using BaiTapWebService.Application.Tenants;
using BaiTapWebService.Domain.Mysql.Tenants;
using BaiTapWebService.Domain.Tenants.Order;
using BaiTapWebService.Domain.Tenants.Video;
using BaiTapWebService.Domain.Tenants.VipPackage;
using BapTapWebService.Domain.Mssql.Tenants;

var builder = WebApplication.CreateBuilder(args);


builder.Services.AddScoped<ISuperUserService, SuperUserService>();
builder.Services.AddScoped<ISuperUserRepo, MssqlSuperUserRepo>();
builder.Services.AddScoped<IVipPackageService, VipPackageService>();
builder.Services.AddScoped<IVipPackageRepo, MssqlVipPackageRepo>();


builder.Services.AddScoped<IVideoService, VideoService>();
builder.Services.AddScoped<IVideoRepo, MysqlVideoRepo>();




// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseCors(builder => builder
    .AllowAnyOrigin()
    .AllowAnyMethod()
    .AllowAnyHeader());

app.UseAuthorization();


app.MapControllers();

app.Run();

//using BaiTapWebService.BusinessApi;

//namespace BaiTapWebService.BusinessAPI
//{
//    public class Program
//    {
//        public static void Main(string[] args)
//        {
//            CreateHostBuilder(args).Build().Run();
//        }

//        public static IHostBuilder CreateHostBuilder(string[] args) =>
//            Host.CreateDefaultBuilder(args)
//            //.ConfigureAppConfiguration(typeof(Program), new string[] {
//            //})
//            .ConfigureWebHostDefaults(webBuilder =>
//             {
//                 webBuilder.UseStartup<Startup>();
//             });
//        //.ConfigureNLog();



//    }
//}

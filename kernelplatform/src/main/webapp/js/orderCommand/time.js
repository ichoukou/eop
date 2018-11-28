
$(function () {
    // 定义一个全局类
    var Merlin = Merlin || {};
    // 缓存页面上的全局变量 params
    Merlin.winParams = window.params || {};
    // 配置日历插件
    Merlin.Date = function () {
        var $startTime = $('#startTime'),
            $endTime = $('#endTime'),
            $dateTip = $('#dateTip');
        // 设置 起始日期
        $startTime.on('click', function () {
            WdatePicker({
                "isShowClear":false, // 是否显示清空按钮
                "readOnly":true, // 是否只读
                "dateFmt":'yyyy-MM-dd',
                "alwaysUseStartDate":true,
                "doubleCalendar":true                    // 双月历
            });
        });
        // 设置 结束日期
        $endTime.on('click', function () {
            WdatePicker({
                "startDate":'#F{$dp.$D(\'startTime\')}', // 开始日期
                "minDate":'#F{$dp.$D(\'startTime\')}', // 最小日期
                "dateFmt":'yyyy-MM-dd',
                "alwaysUseStartDate":true,
                "isShowClear":false, // 是否显示清空按钮
                "readOnly":true, // 是否只读
                "doubleCalendar":true                        // 双月历
            });
        });
    };
    Merlin.Date();          // 配置日历插件
});

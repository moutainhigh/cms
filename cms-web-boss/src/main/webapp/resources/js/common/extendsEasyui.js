/**
 * Extends Easyui textBox 为textBox 添加清空按钮,并且设置默认搞定为30px
 */
$.extend($.fn.textbox.defaults, {
	height : 32,
	/*isClearBtn : true,// 默认添加
	icons : null,*/
});
$.extend($.fn.textbox.methods, {
	/*initValue : function(jq, value) {
		return jq.each(function() {
			var target = $.data(this, "textbox");
			target.options.value = "";
			$(this).textbox("setText", value);
			target.textbox.find(".textbox-value").val(value);
			$(this).val(value);
			if (target.options.isClearBtn) {
				$(this).textbox("addClearBtn", 'icon-clear');
			}
		});
	},
	setText : function(jq, newVal) {
		return jq.each(function() {
			var opts = $(this).textbox("options");
			var textBox = $(this).textbox("textbox");
			newVal = newVal == undefined ? "" : String(newVal);
			if ($(this).textbox("getText") != newVal) {
				textBox.val(newVal);
			}
			opts.value = newVal;
			if (!textBox.is(":focus")) {
				if (newVal) {
					textBox.removeClass("textbox-prompt");
				} else {
					textBox.val(opts.prompt).addClass("textbox-prompt");
				}
			}
			if ("" != newVal && opts.isClearBtn) {
				$(this).textbox('getIcon', 0).css('visibility', 'visible');
			}
			$(this).textbox("validate");
		});
	},
	addClearBtn : function(jq, iconCls) {
		return jq.each(function() {
			var target = $(this);
			var opts = $.data(this, "textbox").options;
			opts.isClearBtn = false;// 未来避免重复添加
			opts.icons = opts.icons || [];
			opts.icons.unshift({
				iconCls : iconCls,
				handler : function(e) {
					$(e.data.target).textbox("clear").textbox("textbox").focus();
					$(this).css('visibility', 'hidden');
				}
			});
			target.textbox();
			if (!target.textbox("getText")) {
				target.textbox("getIcon", 0).css('visibility', 'hidden');
			}
			target.textbox("textbox").bind("keyup", function() {
				var icon = target.textbox('getIcon', 0);
				if ($(this).val()) {
					icon.css('visibility', 'visible');
				} else {
					icon.css('visibility', 'hidden');
				}
			});
		});
	}*/
});
/**
 * Extends Easyui tabs 为tabs 设置默认头部高为30px
 */
$.extend($.fn.tabs.defaults, {
	tabHeight : 32
});
/**
 * Extends Easyui datebox 为datebox 设置默认高为30px
 */
$.extend($.fn.datebox.defaults, {
	height : 32
});

/** * Extends Easyui combo 为combo 设置默认高为30px*** */
$.extend($.fn.combo.defaults, {
	height : 32
});

/** * Extends Easyui combobox 为combobox 设置默认高为30px*** */
$.extend($.fn.combobox.defaults, {
	height : 32
});

/** ************Start validatebox formatter******************** */
$.extend($.fn.validatebox.defaults.rules, {
	// 手机号码
	mobile : {
		validator : function(value, param) {
			return /^0{0,1}1[3,8,5][0-9]{9}$/.test(value);
		},
		message : "手机号码格式不正确"
	},
	// select空值验证
	selectNotNull : {
		validator : function(value, param) {
			return $(param[0]).find("option:contains('" + value + "')").val() != '';
		},
		message : "请选择"
	},
	// 正整数
	pnum : {
		validator : function(value, param) {
			return /^[0-9]*[1-9][0-9]*$/.test(value);
		},
		message : "请输入正整数"
	},
	// 非0开头正整数
	pznum : {
		validator : function(value, param) {
			return /^[1-9]*[1-9][0-9]*$/.test(value);
		},
		message : "请输入非0开头的正整数"
	},
	// 正实数，包含小数
	num : {
		validator : function(value, param) {
			return /^\d+(\.\d+)?$/.test(value);
		},
		message : "请输入正整数或者小数"
	},
	// 2位正整数，或精确两位小数
	numTwoOrPointTwo : {
		validator : function(value, param) {
			return /^([1-9]\d?(\.\d{1,2})?|0\.\d{1,2}|0)$/.test(value);
		},
		message : "请输入1到2位的正整数或者精确到2位的小数"
	},
	// 6位正整数，或精确两位小数
	numSixOrPointTwo : {
		validator : function(value, param) {
			return /^(([0-9]|([1-9][0-9]{0,5}))((\.[0-9]{1,2})?))$/.test(value);
		},
		message : "请输入1到6位的正整数或者精确到2位的小数"
	},
	// 过滤特殊字符
	filterSpecial : {
		validator : function(value, param) {
			var flag = /\s/.test(value);// 过滤空格
			// 过滤特殊字符串
			var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】’‘《》；：”“'。，、？]");
			var specialFlag = pattern.test(value);
			return !flag && !specialFlag;
		},
		message : "非法字符，请重新输入"
	},
	// 身份证
	IDCard : {
		validator : function(value, param) {
			var flag = checkIdcard(value);
			return flag == true ? true : false;
		},
		message : "请输入正确的身份证号码"
	},
	// 比较日期选择器
	compareDate : {
		validator : function(value, param) {
			return dateCompare($(param[0]).datebox("getValue"), value);
		},
		message : "结束日期不能小于或等于开始日期"
	},
	// 比较时间选择器（时分秒）
	compareTime : {
		validator : function(value, param) {
			return timeCompare($(param[0]).timespinner("getValue"), value);
		},
		message : "结束时间不能小于或等于开始时间"
	},
	// 比较时间选择器（时分秒）
	compareDateTime : {
		validator : function(value, param) {
			return dateTimeCompare($(param[0]).timespinner("getValue"), value);
		},
		message : "结束时间不能小于或等于开始时间"
	},
	// 验证是否包含空格和非法字符
	unnormal : {
		validator : function(value) {
			return /^[a-zA-Z0-9]/i.test(value);

		},
		message : '输入值不能为空和包含其他非法字符'
	}

});

/** ************End validatebox formatter******************** */
// 校验身份证合法性
function checkIdcard(idcard) {
	var Errors = new Array("验证通过!", "身份证号码位数不对!", "身份证号码出生日期超出范围或含有非法字符!", "身份证号码校验错误!", "身份证地区非法!");
	var area = {
		11 : "北京",
		12 : "天津",
		13 : "河北",
		14 : "山西",
		15 : "内蒙古",
		21 : "辽宁",
		22 : "吉林",
		23 : "黑龙江",
		31 : "上海",
		32 : "江苏",
		33 : "浙江",
		34 : "安徽",
		35 : "福建",
		36 : "江西",
		37 : "山东",
		41 : "河南",
		42 : "湖北",
		43 : "湖南",
		44 : "广东",
		45 : "广西",
		46 : "海南",
		50 : "重庆",
		51 : "四川",
		52 : "贵州",
		53 : "云南",
		54 : "西藏",
		61 : "陕西",
		62 : "甘肃",
		63 : "青海",
		64 : "宁夏",
		65 : "新疆",
		71 : "台湾",
		81 : "香港",
		82 : "澳门",
		91 : "国外"
	};
	var Y, JYM;
	var S, M;
	var idcard_array = new Array();
	idcard_array = idcard.split("");
	// 地区检验
	if (area[parseInt(idcard.substr(0, 2))] == null) {
		return Errors[4];
	}
	// 身份号码位数及格式检验
	switch (idcard.length) {
	case 15:
		if ((parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0 || ((parseInt(idcard.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0)) {
			ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;// 测试出生日期的合法性
		} else {
			ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;// 测试出生日期的合法性
		}
		if (ereg.test(idcard)) {
			return true;
		} else {
			return Errors[2];
		}
		break;
	case 18:
		// 18位身份号码检测
		// 出生日期的合法性检查
		// 闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
		// 平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
		if (parseInt(idcard.substr(6, 4)) % 4 == 0 || (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard.substr(6, 4)) % 4 == 0)) {
			ereg = /^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;// 闰年出生日期的合法性正则表达式
		} else {
			ereg = /^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;// 平年出生日期的合法性正则表达式
		}
		if (ereg.test(idcard)) {// 测试出生日期的合法性
			// 计算校验位
			S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7 + (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9 + (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10 + (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5 + (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8 + (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4 + (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2 + parseInt(idcard_array[7]) * 1 + parseInt(idcard_array[8]) * 6 + parseInt(idcard_array[9]) * 3;
			Y = S % 11;
			M = "F";
			JYM = "10X98765432";
			M = JYM.substr(Y, 1);// 判断校验位
			if (M == idcard_array[17]) {
				return true;
			} else {
				return Errors[3];
			}
		} else {
			return Errors[2];
		}
		break;
	default:
		return Errors[1];
		break;
	}
}
/*
 * 比较两个日期的大小 传入的参数推荐是"yyyy-mm-dd"的格式，其他的日期格式也可以，但要保证一致
 */
var dateCompare = function(date1, date2) {
	if (date1 && date2) {
		var a = new Date(date1);
		var b = new Date(date2);
		return a < b;
	}
};
/*
 * 比较两个时间的大小（传入的参数是"HH:mm"的格式，） @param: time1:目标时间;time2:被比较时间
 */
var timeCompare = function(time1, time2) {
	// console.info(time1+"-"+time2);
	try {
		if (time1 && time2) {
			var t1 = parseInt(time1.split(":")[0] * 60) + parseInt(time1.split(":")[1]);
			var t2 = parseInt(time2.split(":")[0] * 60) + parseInt(time2.split(":")[1]);
			return t1 < t2;
		}
		return false;
	} catch (e) {
		return false;
	}
};
/*
 * 比较两个时间的大小，支持的格式可在formatArr扩展 @param: datetime1:目标时间;datetime2:被比较时间
 */
var dateTimeCompare = function(datetime1, datetime2) {

	var formatArr = new Array('YYYY-MM-DD', 'YYYY-MM-DD HH:mm', 'YYYY-MM-DD HH:mm:ss');// 支持的格式
	try {
		if (datetime1 && datetime2) {
			var dt1 = moment(datetime1, formatArr);
			var dt2 = moment(datetime2, formatArr);
			return dt1 < dt2;
		}
		return false;
	} catch (e) {
		return false;
	}
};

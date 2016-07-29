/* ======================================================
 *@author zrd
 *@date   2014-09-12
 *描述：根据key获取url中的参数。无此参数则返回null
 * ======================================================
 */
function getUrlParam(url, key){
	var paramArr = url.substring(url.indexOf("?")+1,url.length).split("&");
	for(var i=0;i<paramArr.length;i++){
		var key_value=paramArr[i].split("=");
		if(key_value[0] == key){
			return key_value[1];
		}
	}
	return null;
}
/* ======================================================
 *@author zrd
 *@date   2014-10-22
 *描述：将params拼接url作为参数
 * ======================================================
 */
function appendUrl(url, params){
	var tmp = [];
	for(var p in params){
		if(params[p] !== undefined && params[p] !== null && params[p] !== ""){
			if(getUrlParam(url, p) == null){
				tmp.push(p+"="+params[p]);
			}
		}
	}
	if(tmp.length>0){
		var bFirst = true;
		if(url.indexOf("?") > 0)bFirst = false;
		for(var t=tmp.length-1;t>-1;t--){
			if(bFirst){
				url += "?"+tmp[t];
				bFirst = false;
			}else{
				url += "&"+tmp[t];
			}
		}
	}
	return url;
}
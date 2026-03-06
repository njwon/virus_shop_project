function checkAddProduct(){
	let productId = document.getElementById("productId")
	let pname = document.getElementById("pname")
	let unitPrice = document.getElementById("unitPrice")
	let manufacturer = document.getElementById("manufacturer")
	let category = document.getElementById("category")
	let unitsInStock = document.getElementById("unitsInStock")
	let productImage = document.getElementById("productImage")
	
	/*
	[1]productId:첫글자를 반드시 P로 시작하고 숫자를 조합해서 3-10자리까지 입력
	[2]pname:최소 3자리 최대 12자리까지 입력
	[3]unitPrice: 숫자만 입력, 음수 입력X
	[4]manufacturer, category: 입력이나 선택 필수
	[5]unitsInStock: 숫자만 입력
	[6]productImage: 등록 필수
	*/
	
	function check(regExp, e, msg){
		if(regExp.test(e.value)){
			return true
		}else{
			alert(msg)
			return false
		}
	}
	
	//productId
	if(!check(/^P[0-9]{2,9}$/,productId,"[상품코드]첫글자를 반드시 P로 시작하고 숫자를 조합해서 3-10자리까지 입력해주세요")) {
		productId.select()
		productId.focus()
		return false
		}
	
	//pname
	if(pname.value.length<3 || pname.value.length>12){
		alert("[상품명]최소 3자리 최대 12자리까지 입력하세요.")
		pname.select()
		pname.focus()
		return false
	}
	
	//unitPrice
	if(isNaN(unitPrice.value) || unitPrice.value.length==0 || unitPrice.value<0){
			alert("[상품가격]양의 정수만 입력하세요.")
			unitPrice.select()
			unitPrice.focus()
			return false
	}
	
	//manufacturer
	if(manufacturer.value.trim()==""){
			alert("[제조사]필수 입력사항입니다.")
			manufacturer.select()
			manufacturer.focus()
			return false
	}
	
	//category
	if (!category.value) {
		alert("[카테고리]필수 입력사항입니다.")
		category.select()
		category.focus()
		return false
	}
		
	//unitsInStock
	if(isNaN(unitsInStock.value) || unitsInStock.value.length==0 ){
				alert("[재고수]숫자만 입력하세요.")
				unitsInStock.select()
				unitsInStock.focus()
				return false
		}
	
	//productImage
	if(!(productImage.value))	{
		alert("[제품이미지]필수 입력사항입니다.")
		return false
	}
	
	document.newProduct.submit()
	
}




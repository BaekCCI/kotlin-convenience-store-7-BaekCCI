package store.model

 class Receipt{
    val product : MutableMap<Product,Int> = mutableMapOf()
    val promotionProduct : MutableMap<Product,Int> = mutableMapOf()
    var totalPrice =0
    var discountPromotion =0
    var resultPrice = 0

     fun addProduct(){

     }
     fun addPromotion(product : Product, quantity:Int){
         promotionProduct[product]=promotionProduct.getOrDefault(product, 0) + quantity
     }

}

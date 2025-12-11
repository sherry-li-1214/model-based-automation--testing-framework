function getTLSAuth () {
   
       
        let  tlsAuth= [
            {
              cert: open('/Users/lix25/Documents/api ssl cert/sli-nonprod-postman-(AAA)-nonprod...com 2.pem'),
              key: open('/Users/lix25/Documents/api ssl cert/private.key.pem'),
            },
          ]
            return tlsAuth
    //}
}

export default Object.freeze({
    getTLSAuth
})

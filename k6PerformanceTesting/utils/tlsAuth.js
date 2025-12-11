function getTLSAuth () {
   
       
        let  tlsAuth= [
            {
              cert: open('/Users/com 2.pem'),
              key: open('/Users//private.key.pem'),
            },
          ]
            return tlsAuth
    //}
}

export default Object.freeze({
    getTLSAuth
})

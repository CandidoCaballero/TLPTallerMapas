// User Class
var mongoose = require('mongoose'),  
    Schema = mongoose.Schema;  
  
var userSchema = new Schema({  
    idgcm: String,
    iduser: String
});  
  
//Export the schema  
module.exports = mongoose.model('User', userSchema); 
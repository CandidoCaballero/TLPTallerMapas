module.exports = function(app){  

    var User = require('../models/user');
   
    //Create a new User and save it  
    newUser = function(req, res){ 
        User.findOne({iduser: req.body.iduser}, function(error, user) {
            if (user === null) {
              var user = new User({  iduser: req.body.iduser, 
                               idgcm: req.body.idgcm});  
              user.save();
            }
            res.send(user);  
        });
    };  
  
    //find all users 
    listUsers = function(req, res){  
        User.find({}, function(err, users) {
            res.send(users);  
        });  
    };  
  
    //find user by id  
    findUser = function(req, res) {  
        User.findOne({iduser: req.params.iduser}, function(error, user) {
            res.send(user);  
        });
    };

    
    //Link users and functions  
    app.post('/user', newUser);  
    app.get('/users', listUsers);  
    app.get('/user/:iduser', findUser);

} 
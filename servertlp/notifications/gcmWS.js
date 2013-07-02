module.exports = function(app){  

  var gcm = require('node-gcm')
    , sender = new gcm.Sender('KEY')
    , User = require('../models/user')
   
  register = function(req, res) {
    User.findOne({iduser: req.body.iduser}, function(error, user) {
      if ((user===null) || (user === undefined)){
          var user = new User({ iduser: req.body.iduser,
                                idgcm: req.body.idgcm});  
          user.save();
      } else if (req.body.idgcm !== undefined){
        user.idgcm=req.body.idgcm;
        user.save();
      }
      res.send("true");  
    });
  };
   
  unregister = function(req, res) {
    User.findOne({iduser: req.body.iduser}, function(error, user) {
      user.idgcm=null;
      user.save();
      res.send("true");  
    });
  };
   
  sendMessage = function(req, res) {
    User.findOne({iduser: req.body.receiver}, function(error, user) {
      if (user !== null){
        if ((user.idgcm !== null) && (user.idgcm !== undefined) && (user.idgcm !== "")){
          message = new gcm.Message;
          var registrationIds=[];
          registrationIds.push(user.idgcm);
          var msg = {};
          msg.latitud = req.body.latitud;
          msg.longitud = req.body.longitud;
          var jsonString = JSON.stringify(msg);
          message.addData('message', jsonString);
          sender.send(message, registrationIds, 4, function(error, result) {
             if (error === null)
                res.send("true");
             else{
                console.log(result);
                res.send("false");
             }
          });
        } else{
          res.send("notRegistered");  
        }
      } else {
          res.send("false");
      }
    });
  };

  app.post('/gcm/register',   register);
  app.post('/gcm/unregister', unregister);
  app.post('/gcm/send',       sendMessage);

} 
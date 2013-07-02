//App routes  
module.exports = function(app){  

    require('../webservices/userWS')(app); 
    require('../notifications/gcmWS')(app);

}  
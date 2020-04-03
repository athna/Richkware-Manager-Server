package it.richkmeli.rms.web.account;

import it.richkmeli.jframework.auth.web.account.LogInJob;
import it.richkmeli.jframework.auth.web.util.AuthServletManager;
import it.richkmeli.jframework.network.tcp.server.http.util.JServletException;
import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.jframework.util.log.Logger;
import it.richkmeli.rms.data.rmc.model.RMC;
import it.richkmeli.rms.web.util.RMSServletManager;
import it.richkmeli.rms.web.util.RMSSession;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LogIn")
public class LogIn extends HttpServlet {
    LogInJob logIn = new LogInJob() {

        @Override
        protected void doSpecificAction(AuthServletManager authServletManager) throws JServletException, DatabaseException {
            //RMSServletManager rmsServletManager = new RMSServletManager(request,response);
            RMSServletManager rmsServletManager = new RMSServletManager(authServletManager);
            RMSSession rmsSession = rmsServletManager.getRMSServerSession();
            if (rmsSession != null) {
                if (rmsSession.getChannel() != null) {
                    if (rmsSession.getChannel().equalsIgnoreCase(RMSServletManager.Channel.RMC)) {
                        RMC rmc = new RMC(rmsSession.getUserID(), rmsSession.getRmcID());
                        Logger.info("RMC: " + rmc.getAssociatedUser() + " - " + rmc.getRmcId());
                        if (!rmsSession.getRmcDatabaseManager().checkRmcUserPair(rmc)) {
                            if (rmsSession.getRmcDatabaseManager().checkRmcUserPair(new RMC("", rmsSession.getRmcID()))) {
                                rmsSession.getRmcDatabaseManager().editRMC(rmc);
                            } else {
                                rmsSession.getRmcDatabaseManager().addRMC(rmc);
                            }
                        }
                    }
                } else {
                    Logger.error("channel rmsSession is null");
                }
            } else {
                Logger.error("rmsSession is null");
            }
        }
    };

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        RMSServletManager rmsServletManager = new RMSServletManager(request, response);
        logIn.doAction(rmsServletManager);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        RMSServletManager rmsServletManager = new RMSServletManager(request, response);
        logIn.doAction(rmsServletManager);
    }
}

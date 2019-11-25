package it.richkmeli.rms.web.crypto;

import it.richkmeli.rms.data.rmc.model.RMC;
import it.richkmeli.rms.web.util.RMSServletManager;
import it.richkmeli.rms.web.util.RMSSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/SecureConnection")
public class SecureConnection extends HttpServlet {
    it.richkmeli.jframework.web.crypto.SecureConnection secureConnection = new it.richkmeli.jframework.web.crypto.SecureConnection() {
        RMSSession rmsSession;

        @Override
        protected void doBeforeCryptoAction(HttpServletRequest request, String clientID) throws Exception {
            RMSServletManager rmsServletManager = new RMSServletManager(request);
            rmsServletManager.getRMSServerSession();

            rmsSession.setRmcID(clientID);
        }

        @Override
        protected void doFinalCryptoAction() throws Exception {

            //TODO fare controllo se rmcId è già presente. se sì, allora non fare la add
            if (rmsSession.getRmcID() != null) {
                if (!rmsSession.getRmcDatabaseManager().checkRmc(rmsSession.getRmcID())) {
                    rmsSession.getRmcDatabaseManager().addRMC(new RMC("", rmsSession.getRmcID()));
                }
            }
        }
    };

    public SecureConnection() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        secureConnection.doAction(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        secureConnection.doAction(request, response);
    }
}

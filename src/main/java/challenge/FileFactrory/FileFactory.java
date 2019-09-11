package challenge.FileFactrory;

public class FileFactory {

    public JsonFile getFile(String fileType){
        if(fileType == null){
            return null;
        }
        if(fileType.equalsIgnoreCase("USER")){
            return new UserJsonFile();

        } else if(fileType.equalsIgnoreCase("ORGANIZATION")){
            return new OrganizationJsonFile();

        } else if(fileType.equalsIgnoreCase("TICKET")){
            return new TicketJsonFile();
        }

        return null;
    }
}

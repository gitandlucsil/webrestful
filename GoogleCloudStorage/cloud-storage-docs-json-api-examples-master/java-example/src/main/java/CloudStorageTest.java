import java.io.File;
import java.util.List;

import com.google.api.services.storage.model.Bucket;
import com.google.api.services.storage.model.StorageObject;

public class CloudStorageTest {
	
	private static final String BUCKET_NAME = "livroandre";
	
	public static void main(String[] args) throws Exception {
		CloudStorageUtil c = new CloudStorageUtil("Livro Andre");
		//Campo Service account ID criado no console
		String accountId = "compute-engine@tranquil-lotus-221723.iam.gserviceaccount.com";
		//Arquivo .p12 baixado no console no momento de criar a chave
		File p12File = new File("My First Project-5b856fecf571.p12");
		//COnect
		c.connect(accountId, p12File);
		//Consulta o bucket
		Bucket bucket = c.getBucket(BUCKET_NAME);
		System.out.println("name: "+bucket.getName());
		System.out.println("location: "+bucket.getLocation());
		System.out.println("timeCreated: "+bucket.getTimeCreated());
		System.out.println("owner: "+bucket.getOwner());
		//Lista os arquivos
		List<StorageObject> files = c.getBucketFiles(bucket);
		for(StorageObject object : files) {
			System.out.println("> "+object.getName()+ " ("+object.getSize()+" bytes)");
		}
	}
}

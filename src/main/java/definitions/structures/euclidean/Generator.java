package definitions.structures.euclidean;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import definitions.Unweavable;
import definitions.cache.MyCache;
import definitions.settings.XmlPrintable;
import definitions.structures.abstr.algebra.fields.impl.FieldGenerator;
import definitions.structures.abstr.algebra.groups.GroupGenerator;
import definitions.structures.euclidean.mappings.impl.MappingGenerator;
import definitions.structures.euclidean.vectorspaces.impl.SpaceGenerator;
import plotter.Plotter;
import settings.GlobalSettings;

@Service
@ComponentScan(basePackages = "definitions")
public class Generator implements IGenerator, Unweavable, Plotter, XmlPrintable {

	private static boolean restoreFromCached = GlobalSettings.RESTORE_FROM_CACHED;

	private static final long serialVersionUID = -5553433829703982950L;

	private static Generator instance;

	public static synchronized Generator getInstance() {
		if (instance == null) {
			instance = new Generator();
			if (instance.logger == null) {
				instance.logger = LogManager.getLogger(SpaceGenerator.class); 
			}
		}
		if (restoreFromCached) {
			try {
				instance.loadCoordinateSpaces();
				System.out.println("Cached spaces loaded");
			} catch (final Exception e) {
				System.out.println("Cached spaces not loaded");
			}
			restoreFromCached = false;
		}
		return instance;
	}

	public static void setInstance(final Generator instance) {
		Generator.instance = instance;
		MappingGenerator.setInstance(instance.mappingGenerator);
		SpaceGenerator.setInstance(instance.spaceGenerator);
		FieldGenerator.setInstance(instance.fieldGenerator);
		GroupGenerator.setInstance(instance.groupGenerator);
	}

	private Logger logger;

	private final String PATH = GlobalSettings.CACHEDSPACES;

	@Autowired(required = true)
	private MappingGenerator mappingGenerator;

	@Autowired(required = true)
	private SpaceGenerator spaceGenerator;

	@Autowired
	private FieldGenerator fieldGenerator;

	@Autowired
	private GroupGenerator groupGenerator;

	@Override
	public FieldGenerator getFieldGenerator() {
		return this.fieldGenerator;
	}

	@Override
	public GroupGenerator getGroupGenerator() {
		return this.groupGenerator;
	}

	public Logger getLogger() {
		if (this.logger == null) {
			this.logger = LogManager.getLogger(this.getClass());
		}
		return this.logger;
	}

	@Override
	public MappingGenerator getMappingGenerator() {
		return this.mappingGenerator;
	}

	@Override
	public SpaceGenerator getSpaceGenerator() {
		return this.spaceGenerator;
	}

	@Override
	public void loadCoordinateSpaces() throws IOException, ClassNotFoundException {
		try {
			final FileInputStream f_in = new FileInputStream(this.PATH);
			final ObjectInputStream obj_in = new ObjectInputStream(f_in);
			final MyCache ans = (MyCache) obj_in.readObject();
			this.spaceGenerator.setMyCache(ans);
			obj_in.close();
		} catch (final Exception e) {
			e.addSuppressed(new Exception("failed to load myCache from local file"));
			getLogger().info("Cached spaces not loaded");
		}
	}

	@Override
	public void saveCoordinateSpaces() throws IOException {
		final FileOutputStream f_out = new FileOutputStream(this.PATH);
		final ObjectOutputStream obj_out = new ObjectOutputStream(f_out);
		obj_out.writeObject(this.spaceGenerator.getMyCache());
		this.getLogger().info("saved coordinates spaces to disk");
		obj_out.close();
	}

	@Override
	public void setFieldGenerator(final FieldGenerator fieldGenerator) {
		this.fieldGenerator = fieldGenerator;
	}

	public void setGroupGenerator(final GroupGenerator groupGenerator) {
		GroupGenerator.setInstance(groupGenerator);
		this.groupGenerator = groupGenerator;
	}

	@Override
	public String toXml() {
		return "the main generator";
	}
}
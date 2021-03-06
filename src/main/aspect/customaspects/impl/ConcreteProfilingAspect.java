package customaspects.impl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.DeclarePrecedence;

import customaspects.AbstractCustomAspect;
import customaspects.ProfilingAspect;
import definitions.settings.XmlPrintable;

/**
 * Ein umfangreicher Aspekt zum hierarchischen xml-Logging aller Ausfuehrungen
 * innerhalb einer speziellen Ausfuehrung.
 */
@Aspect
@DeclarePrecedence(value = "ConcreteTestAspect,ConcreteProfilingAspect")
public class ConcreteProfilingAspect extends AbstractCustomAspect implements ProfilingAspect {

	/**
	 * maximale Suchtiefe
	 */
	private final int maximalDepth = 5;

	/**
	 * @return the maximalDepth
	 */
	@Override
	public int getMaximalDepth() {
		return maximalDepth;
	}

	public ConcreteProfilingAspect() {
		register();
		setGenericName("ProfilingAspect");
		this.setThreadToOutputMap(new ConcurrentHashMap<Thread, List<String>>());
	}

	/**
	 * Der Pointcut der Methoden von Interesse
	 */
	private final String included = "execution(* definitions..*.*(..))";

	/**
	 * Der resultierende Pointcut
	 */
	private final String pointcut = included;

	/**
	 * Der Eintritts-Joinpoint, hier durch die org.junit.Test-Annotation markiert.
	 */
	private final String initialJoinPoint = "@annotation(org.junit.Test)";

	/**
	 * Vor dem Betreten des Eintritts-Joinpoint wird der Schalter "running"
	 * umgelegt.
	 *
	 * @param jp der Eintritts-Joinpoint
	 */
	@Override
	@Before(initialJoinPoint)
	public void beforeInitialJoinPoint(JoinPoint jp) {
		ProfilingAspect.super.beforeInitialJoinPoint(jp);
	}

	@Override
	@Around(pointcut)
	public Object recordMethodExecutionAdvise(final ProceedingJoinPoint pjp) throws Throwable {
		return ProfilingAspect.super.recordMethodExecutionAdvise(pjp);
	}

	/**
	 * Hauptschalter. Nur wenn true, passiert irgend etwas
	 */
	private volatile Boolean enabled = true;

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Kontrollschalter
	 */
	private volatile boolean recording = false;

	@Override
	public boolean isRecording() {
		return recording;
	}

	@Override
	public void setRecording(boolean recording) {
		this.recording = recording;
	}

	/**
	 * Aktuelle Suchtiefe
	 */
	private volatile int currentDepth = 0;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCurrentDepth() {
		return currentDepth;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCurrentDepth(int depth) {
		this.currentDepth = depth;
	}

	/**
	 * Instantane Ausgaben auf die Konsole
	 */
	private boolean loggingToConsole = false;

	/**
	 * @return the loggingToConsole
	 */
	@Override
	public boolean isLoggingToConsole() {
		return loggingToConsole;
	}

	@Override
	public void setIsLoggingToConsole(boolean logging) {
		loggingToConsole = logging;
	}

	/**
	 * Methode soll nur einmal aufgezeichnet werden?
	 */
	private boolean runOnlyOnce = true;

	/**
	 * @return the runOnlyOnce
	 */
	@Override
	public boolean isRunOnlyOnce() {
		return runOnlyOnce;
	}

	/**
	 * @param runOnlyOnce the runOnlyOnce to set
	 */
	@Override
	public void setRunOnlyOnce(boolean runOnlyOnce) {
		this.runOnlyOnce = runOnlyOnce;
	}

	@Override
	public void register() {
		ConcreteTestAspect.getInstance().getRelevantAspects().add(this);
	}

	@Override
	public String stringOf(Object o) {
		setRecording(false);
		String ans = "";
		if (o instanceof XmlPrintable) {
			ans = ((XmlPrintable) o).toXml();
		} else {
			ans = ProfilingAspect.super.stringOf(o);
		}
		setRecording(true);
		return ans;
	}
}

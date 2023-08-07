package it.csi.nembo.nemboconf.integration;

public interface IIterableOfPersistent extends IPersistent
{
  public Iterable<IPersistent> getIterable();
}

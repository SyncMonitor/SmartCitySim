package it.synclab.smartcitysim.interfaces;

public interface Entity<IdType> {
    public IdType getId();
    public void setId(IdType id);
}

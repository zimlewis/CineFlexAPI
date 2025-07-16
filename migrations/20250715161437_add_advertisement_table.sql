-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Advertisement] (
    [Id]         UNIQUEIDENTIFIER NOT NULL CONSTRAINT PK_Advertisement         PRIMARY KEY,
    [Link]       VARCHAR(100)     NOT NULL,
    [Image]      VARCHAR(100)     NOT NULL,
    [Enabled]    BIT              NOT NULL CONSTRAINT DF_Advertisement_Enabled DEFAULT 1,
    [Type]       INT              NOT NULL,
    [CreatedTime]DATETIME,
    [UpdatedTime]DATETIME,
    [Hirer]      UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_Advertisement_Hirer   FOREIGN KEY REFERENCES [dbo].[Hirer]([Id])
);
-- +goose StatementEnd


-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Advertisement];
-- +goose StatementEnd
